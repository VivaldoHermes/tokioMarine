<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import { useTransferStore, type TransferRequest } from '../stores/transfer'
import { useToast } from '../composables/useToast'
import { parseMoneyBRL, formatMoneyBRL } from '../utils/format'

const store = useTransferStore()
const { success: toastSuccess, error: toastError } = useToast()

function todayLocalISO(): string {
  const d = new Date()
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

const today = todayLocalISO()

const form = ref<TransferRequest>({
  sourceAccount: '',
  destinationAccount: '',
  amount: 0,
  scheduledDate: today,
})

const amountInput = ref<string>(formatMoneyBRL(form.value.amount))
const amountEl = ref<HTMLInputElement | null>(null)

const submitting = ref(false)
const error = ref<string | null>(null)
const success = ref(false)

type Field = 'sourceAccount' | 'destinationAccount' | 'amount' | 'scheduledDate'

const errors = reactive<Record<Field, string>>({
  sourceAccount: '',
  destinationAccount: '',
  amount: '',
  scheduledDate: '',
})

const touched = reactive<Record<Field, boolean>>({
  sourceAccount: false,
  destinationAccount: false,
  amount: false,
  scheduledDate: false,
})

function validateField(field: Field) {
  const f = form.value
  switch (field) {
    case 'sourceAccount':
      errors.sourceAccount = f.sourceAccount.trim() ? '' : 'Source account is required.'
      break
    case 'destinationAccount':
      errors.destinationAccount = f.destinationAccount.trim()
        ? ''
        : 'Destination account is required.'
      break
    case 'amount':
      if (f.amount === null || f.amount === undefined || Number.isNaN(f.amount)) {
        errors.amount = 'Amount is required.'
      } else if (Number(f.amount) <= 0) {
        errors.amount = 'Amount must be greater than 0.'
      } else {
        errors.amount = ''
      }
      break
    case 'scheduledDate':
      if (!f.scheduledDate) {
        errors.scheduledDate = 'Scheduled date is required.'
      } else if (f.scheduledDate < today) {
        errors.scheduledDate = 'Scheduled date cannot be in the past.'
      } else {
        errors.scheduledDate = ''
      }
      break
  }
}

function validateAll() {
  ;(['sourceAccount', 'destinationAccount', 'amount', 'scheduledDate'] as Field[]).forEach((k) =>
    validateField(k),
  )
}

const isValid = computed(
  () =>
    Object.values(errors).every((msg) => msg === '') &&
    form.value.sourceAccount.trim() &&
    form.value.destinationAccount.trim() &&
    Number(form.value.amount) > 0 &&
    form.value.scheduledDate >= today,
)

watch(
  () => form.value,
  () => {
    ;(Object.keys(touched) as Field[]).forEach((k) => {
      if (touched[k]) validateField(k)
    })
  },
  { deep: true },
)

function onBlur(field: Field) {
  touched[field] = true
  validateField(field)
}

function onAmountFocus() {
  amountInput.value = form.value.amount ? String(form.value.amount) : ''
}
function onAmountInput(e: Event) {
  const t = e.target as HTMLInputElement
  amountInput.value = t.value
  form.value.amount = parseMoneyBRL(t.value)
}
function onAmountBlur() {
  validateField('amount')
  amountInput.value = formatMoneyBRL(form.value.amount)
}

async function submit() {
  error.value = null
  success.value = false

  touched.sourceAccount = touched.destinationAccount = touched.amount = touched.scheduledDate = true
  validateAll()
  if (!isValid.value) return

  submitting.value = true
  try {
    await store.create(form.value)
    success.value = true
    toastSuccess('Transfer scheduled successfully.')
    form.value.amount = 0
    form.value.scheduledDate = today
    errors.amount = ''
    errors.scheduledDate = ''
    amountInput.value = formatMoneyBRL(0)
  } catch {
    error.value = store.error ?? 'Failed to schedule transfer'
    toastError(error.value)
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <form @submit.prevent="submit" style="display: grid; gap: 12px; max-width: 480px">
    <label style="display: grid; gap: 6px">
      <span>Source account</span>
      <input
        v-model="form.sourceAccount"
        @blur="onBlur('sourceAccount')"
        required
        autocomplete="off"
      />
      <small v-if="touched.sourceAccount && errors.sourceAccount" style="color: #c00">
        {{ errors.sourceAccount }}
      </small>
    </label>

    <label style="display: grid; gap: 6px">
      <span>Destination account</span>
      <input
        v-model="form.destinationAccount"
        @blur="onBlur('destinationAccount')"
        required
        autocomplete="off"
      />
      <small v-if="touched.destinationAccount && errors.destinationAccount" style="color: #c00">
        {{ errors.destinationAccount }}
      </small>
    </label>

    <label style="display: grid; gap: 6px">
      <span>Amount</span>
      <input
        id="amount-input"
        ref="amountEl"
        :value="amountInput"
        @focus="onAmountFocus"
        @input="onAmountInput"
        @blur="onAmountBlur"
        inputmode="decimal"
        autocomplete="off"
        required
      />
      <small v-if="touched.amount && errors.amount" style="color: #c00">
        {{ errors.amount }}
      </small>
    </label>

    <label style="display: grid; gap: 6px">
      <span>Scheduled date</span>
      <input
        v-model="form.scheduledDate"
        type="date"
        :min="today"
        required
        @blur="onBlur('scheduledDate')"
      />
      <small v-if="touched.scheduledDate && errors.scheduledDate" style="color: #c00">
        {{ errors.scheduledDate }}
      </small>
    </label>

    <button :disabled="submitting || !isValid" type="submit">
      {{ submitting ? 'Scheduling...' : 'Schedule' }}
    </button>
  </form>
</template>
