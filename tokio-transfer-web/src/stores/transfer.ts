import { defineStore } from 'pinia'
import api from '../services/api'

export interface Transfer {
  id: string
  sourceAccount: string
  destinationAccount: string
  amount: number
  fee: number
  status: string
  transferDate: string
  scheduledDate: string
}

export interface TransferRequest {
  sourceAccount: string
  destinationAccount: string
  amount: number
  scheduledDate: string
}

function hasMessage(e: unknown): e is { message: string } {
  return (
    typeof e === 'object' &&
    e !== null &&
    'message' in e &&
    typeof (e as Record<string, unknown>).message === 'string'
  )
}

export const useTransferStore = defineStore('transfer', {
  state: () => ({
    items: [] as Transfer[],
    loading: false as boolean,
    error: null as string | null,
  }),

  actions: {
    async fetchAll() {
      this.loading = true
      this.error = null
      try {
        const { data } = await api.get<Transfer[]>('/transfers')
        this.items = data
      } catch (err: unknown) {
        this.error = hasMessage(err) ? err.message : 'Failed to load transfers'
        throw err
      } finally {
        this.loading = false
      }
    },

    async create(payload: TransferRequest) {
      this.loading = true
      this.error = null
      try {
        // backend espera transferDate
        const dto = {
          sourceAccount: payload.sourceAccount,
          destinationAccount: payload.destinationAccount,
          amount: payload.amount,
          transferDate: payload.scheduledDate, // 👈 mapeamento correto
        }

        const { data } = await api.post<Transfer>('/transfers', dto)
        this.items.unshift(data)
        return data
      } catch (err: unknown) {
        this.error = hasMessage(err) ? err.message : 'Failed to schedule transfer'
        throw err
      } finally {
        this.loading = false
      }
    },
  },
})
