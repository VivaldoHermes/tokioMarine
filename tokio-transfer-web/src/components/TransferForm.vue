<script setup lang="ts">
import { ref } from 'vue'
import { useTransferStore, type TransferRequest } from '../stores/transfer'

const store = useTransferStore()

const today = new Date().toISOString().slice(0, 10)

const form = ref<TransferRequest>({
  sourceAccount: '',
  destinationAccount: '',
  amount: 0,
  scheduledDate: today,
})

const submitting = ref(false)
const error = ref<string | null>(null)
const success = ref(false)

async function onSubmit() {
  error.value = null
  success.value = false
  submitting.value = true
  try {
    await store.create(form.value)
    success.value = true
    form.value.amount = 0
    form.value.scheduledDate = today
  } catch {
    error.value = store.error ?? 'Failed to schedule transfer'
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <form @submit.prevent="onSubmit" style="display: grid; gap: 12px; max-width: 480px">
    <label style="display: grid; gap: 6px">
      <span>Source account</span>
      <input v-model="form.sourceAccount" required />
    </label>

    <label style="display: grid; gap: 6px">
      <span>Destination account</span>
      <input v-model="form.destinationAccount" required />
    </label>

    <label style="display: grid; gap: 6px">
      <span>Amount</span>
      <input v-model.number="form.amount" type="number" step="0.01" min="0" required />
    </label>

    <label style="display: grid; gap: 6px">
      <span>Scheduled date</span>
      <input v-model="form.scheduledDate" type="date" required />
    </label>

    <button :disabled="submitting" type="submit">
      {{ submitting ? 'Scheduling...' : 'Schedule' }}
    </button>

    <p v-if="error" style="color: #c00">{{ error }}</p>
    <p v-else-if="success" style="color: #0a0">Transfer scheduled successfully.</p>
  </form>
</template>
