<template>
  <div class="table-card">
    <div class="table-toolbar">
      <h2>Transferências agendadas</h2>
      <div class="spacer"></div>
      <button class="btn-refresh" @click="refresh" :disabled="store.loading">Atualizar</button>
    </div>

    <!-- Loading -->
    <div v-if="store.loading" class="skeleton">
      <div v-for="n in 6" :key="n" class="skeleton-row"></div>
    </div>

    <!-- Erro -->
    <div v-else-if="store.error" class="error-card">{{ store.error }}</div>

    <!-- Vazio -->
    <div v-else-if="items.length === 0" class="empty-card">Nenhuma transferência encontrada.</div>

    <!-- Tabela -->
    <div v-else class="table-wrap">
      <table class="table-list">
        <thead>
          <tr>
            <th>ID</th>
            <th>Origem</th>
            <th>Destino</th>
            <th>Valor</th>
            <th>Agendada</th>
            <th>Taxa</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="t in items" :key="t.id">
            <td class="mono truncate" :title="t.id">{{ t.id }}</td>
            <td class="truncate" :title="t.sourceAccount">{{ t.sourceAccount }}</td>
            <td class="truncate" :title="t.destinationAccount">{{ t.destinationAccount }}</td>
            <td>{{ formatMoneyBRL(t.amount) }}</td>
            <td>{{ formatISODateDDMMYYYY(t.scheduledDate, '-') }}</td>
            <td>{{ formatMoneyBRL(t.fee) }}</td>
            <td>
              <span class="badge" :data-variant="statusVariant(t.status)">
                {{ t.status }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { useTransferStore, type Transfer } from '../stores/transfer'
import { formatMoneyBRL, formatISODateDDMMYYYY } from '../utils/format'

const store = useTransferStore()

onMounted(() => {
  if (!store.loading && store.items.length === 0) {
    store.fetchAll()
  }
})

const items = computed<Transfer[]>(() => store.items)

function refresh() {
  store.fetchAll()
}

function statusVariant(s?: string) {
  const k = (s || '').toLowerCase()
  if (k.includes('cancel') || k.includes('reject') || k.includes('error') || k.includes('fail'))
    return 'danger'
  if (k.includes('pend') || k.includes('sched') || k === 'created') return 'pending'
  if (
    k.includes('ok') ||
    k.includes('done') ||
    k.includes('execut') ||
    k.includes('success') ||
    k.includes('settled') ||
    k.includes('complet')
  )
    return 'success'
  return 'neutral'
}
</script>

<style src="./TransfersTable.css"></style>
