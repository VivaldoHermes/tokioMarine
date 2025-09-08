<template>
  <div class="schedule">
    <!-- 1) Faixa branca com logo (link para Home) -->
    <header class="bar-logo">
      <div class="container logo-inner">
        <RouterLink to="/" class="logo-link" aria-label="Voltar para Home">
          <img class="logo" alt="Tokio Marine Seguradora" :src="TM_LOGO_URL" />
        </RouterLink>
        <div class="logo-spacer"></div>
      </div>
    </header>

    <!-- 2) Faixa degradê -->
    <div class="bar-gradient">
      <div class="container gradient-inner"><div /></div>
    </div>

    <!-- 3) Faixa larga com imagem + overlay + texto -->
    <section class="banner">
      <div class="banner-bg" />
      <div class="banner-overlay" />
      <div class="container banner-content">
        <p class="kicker">AGENDAR TRANSFERÊNCIA</p>
        <h1 class="headline">Organize suas transferências com praticidade</h1>
        <p class="sub">Insira os dados necessários para agendar sua transferência com segurança.</p>
      </div>
    </section>

    <!-- 4) Formulário em cartões -->
    <section class="form-wrap">
      <div class="container">
        <form class="cards-grid" @submit.prevent="onSubmit">
          <!-- Conta de origem -->
          <label class="card-input">
            <span class="card-label">Conta de origem</span>
            <input
              v-model.trim="form.source"
              placeholder="Digite aqui"
              required
              class="card-field"
              type="text"
              name="source"
              autocomplete="off"
            />
          </label>

          <!-- Conta de destino -->
          <label class="card-input">
            <span class="card-label">Conta de destino</span>
            <input
              v-model.trim="form.destination"
              placeholder="Digite aqui"
              required
              class="card-field"
              type="text"
              name="destination"
              autocomplete="off"
            />
          </label>

          <!-- Valor -->
          <label class="card-input">
            <span class="card-label">Valor</span>
            <input
              :value="amountDisplay"
              @focus="onAmountFocus"
              @input="onAmountInput"
              @blur="onAmountBlur"
              placeholder="R$ 0,00"
              inputmode="numeric"
              class="card-field"
              name="amount"
              autocomplete="off"
              required
            />
          </label>

          <!-- Data agendada -->
          <label class="card-input">
            <span class="card-label">Data agendada</span>
            <input
              v-model="form.date"
              :min="today"
              class="card-field"
              name="date"
              type="date"
              required
            />
          </label>

          <div class="actions">
            <button class="btn-primary" type="submit" :disabled="submitting || !canSubmit">
              {{ submitting ? 'Agendando...' : 'Agendar' }}
            </button>
          </div>
        </form>
      </div>
    </section>

    <!-- 5) Modal de resultado (sucesso/erro) -->
    <div
      v-if="showResult"
      class="result-backdrop"
      role="dialog"
      aria-modal="true"
      @click.self="closeResult"
    >
      <div class="result-card" :data-type="result.type">
        <div class="result-icon" aria-hidden="true">
          <span v-if="result.type === 'success'">✓</span>
          <span v-else>!</span>
        </div>
        <h3 class="result-title">
          {{ result.type === 'success' ? 'Transferência agendada' : 'Não foi possível agendar' }}
        </h3>

        <p v-if="result.message" class="result-message" aria-live="assertive">
          {{ result.message }}
        </p>

        <ul v-if="result.details" class="result-details">
          <li><strong>De:</strong> {{ result.details.source }}</li>
          <li><strong>Para:</strong> {{ result.details.destination }}</li>
          <li><strong>Valor:</strong> {{ result.details.amount }}</li>
          <li><strong>Data:</strong> {{ result.details.date }}</li>
        </ul>

        <div class="result-actions">
          <button class="btn-primary" @click="closeResult">OK</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, computed, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { useTransferStore, type TransferRequest } from '../stores/transfer'
import { useToast } from '../composables/useToast'
import { parseMoneyBRL, formatMoneyBRL } from '../utils/format'

const store = useTransferStore()
const { success: toastSuccess, error: toastError } = useToast()

const TM_LOGO_URL =
  'https://www.tokiomarine.com.br/wp-content/themes/tokiomarine/images/icons/common/logo-tokiomarine.svg'

const today = new Date().toISOString().slice(0, 10)

const form = reactive({
  source: '',
  destination: '',
  amountNumber: 0,
  amountRaw: '',
  date: today,
})

const submitting = ref(false)

/* ---------- Modal de resultado ---------- */
const showResult = ref(false)
const result = reactive<{
  type: 'success' | 'error'
  message: string
  details: null | { source: string; destination: string; amount: string; date: string }
}>({
  type: 'success',
  message: '',
  details: null,
})

function openSuccess() {
  result.type = 'success'
  result.message = 'Sua transferência foi registrada com sucesso.'
  result.details = {
    source: form.source.trim(),
    destination: form.destination.trim(),
    amount: formatMoneyBRL(form.amountNumber),
    date: form.date,
  }
  showResult.value = true
}

function openError(message: string) {
  result.type = 'error'
  result.message = message
  result.details = null
  showResult.value = true
}

function closeResult() {
  showResult.value = false
}

/* ---------- Campo Valor (edita sem "R$ 0,00") ---------- */
const isEditingAmount = ref(false)

const amountDisplay = computed(() =>
  isEditingAmount.value ? form.amountRaw : formatMoneyBRL(form.amountNumber),
)

function onAmountFocus() {
  isEditingAmount.value = true
  form.amountRaw = form.amountNumber > 0 ? String(form.amountNumber).replace('.', ',') : ''
}

function onAmountInput(e: Event) {
  form.amountRaw = (e.target as HTMLInputElement).value
}

function onAmountBlur() {
  form.amountNumber = parseMoneyBRL(form.amountRaw)
  form.amountRaw = ''
  isEditingAmount.value = false
}

/* ---------- Validação & submit ---------- */
const canSubmit = computed(
  () =>
    !!form.source &&
    !!form.destination &&
    !!form.date &&
    form.amountNumber > 0 &&
    form.date >= today,
)

/* ---------- Erro amigável do backend (captura arrays) ---------- */
interface BackendErrorItem {
  field?: string
  message?: string
  defaultMessage?: string
}
interface BackendErrorData {
  message?: string
  error?: string
  errors?: BackendErrorItem[]
  violations?: BackendErrorItem[]
  details?: string | string[]
  title?: string
}

function extractFriendlyError(err: unknown): string {
  const fallback =
    store.error ?? 'Não foi possível agendar a transferência. Verifique os dados e tente novamente.'

  const isNonEmpty = (v: unknown): v is string => typeof v === 'string' && v.trim().length > 0
  const collect = (arr?: BackendErrorItem[]) =>
    Array.isArray(arr)
      ? arr
          .map((i) => i?.message || i?.defaultMessage)
          .filter(isNonEmpty)
          .join('\n')
      : ''

  if (typeof err === 'object' && err !== null) {
    const obj = err as Record<string, unknown>
    const response = obj['response'] as Record<string, unknown> | undefined
    const data = (response?.['data'] ?? obj['data']) as BackendErrorData | undefined

    if (data) {
      const byErrors = collect(data.errors)
      if (isNonEmpty(byErrors)) return byErrors

      const byViolations = collect(data.violations)
      if (isNonEmpty(byViolations)) return byViolations

      if (isNonEmpty(data.message)) return data.message.trim()
      if (isNonEmpty(data.error)) return data.error.trim()

      if (Array.isArray(data.details)) {
        const joined = data.details.filter(isNonEmpty).join('\n')
        if (isNonEmpty(joined)) return joined
      }
      if (isNonEmpty(data.details)) return String(data.details).trim()
      if (isNonEmpty(data.title)) return data.title.trim()
    }
  }
  return fallback
}

async function onSubmit() {
  if (!canSubmit.value || submitting.value) return
  submitting.value = true

  const payload: TransferRequest = {
    sourceAccount: form.source.trim(),
    destinationAccount: form.destination.trim(),
    amount: form.amountNumber,
    scheduledDate: form.date,
  }

  try {
    await store.create(payload)
    toastSuccess('Transferência agendada com sucesso.')
    openSuccess()

    // limpa formulário
    form.source = ''
    form.destination = ''
    form.amountNumber = 0
    form.amountRaw = ''
    form.date = today
  } catch (err: unknown) {
    const msg = extractFriendlyError(err)
    toastError(msg)
    openError(msg)
  } finally {
    submitting.value = false
  }
}
</script>

<style src="./ScheduleTransferView.css"></style>
