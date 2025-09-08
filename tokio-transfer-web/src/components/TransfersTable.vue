<script setup lang="ts">
import { onMounted, computed } from 'vue'
import { useTransferStore, type Transfer } from '../stores/transfer'
import { formatMoneyBRL, formatISODateYYYYMMDD } from '../utils/format'

const store = useTransferStore()

onMounted(() => {
  if (!store.loading && store.items.length === 0) {
    store.fetchAll()
  }
})

const items = computed<Transfer[]>(() => store.items)
</script>

<template>
  <div>
    <p v-if="store.loading">Loading...</p>
    <p v-else-if="store.error" style="color: #c00">{{ store.error }}</p>
    <p v-else-if="items.length === 0">No transfers found.</p>

    <table v-else border="1" cellpadding="6" cellspacing="0" style="width: 100%">
      <thead>
        <tr>
          <th>ID</th>
          <th>Source</th>
          <th>Destination</th>
          <th>Amount</th>
          <th>Scheduled</th>
          <th>Fee</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="t in items" :key="t.id">
          <td>{{ t.id }}</td>
          <td>{{ t.sourceAccount }}</td>
          <td>{{ t.destinationAccount }}</td>
          <td>{{ formatMoneyBRL(t.amount) }}</td>
          <td>{{ formatISODateYYYYMMDD(t.scheduledDate) }}</td>
          <td>{{ formatMoneyBRL(t.fee) }}</td>
          <td>{{ t.status }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
