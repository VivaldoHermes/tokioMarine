import { ref } from 'vue'

export type ToastType = 'success' | 'error' | 'info'
export interface Toast {
  id: number
  type: ToastType
  message: string
  timeout: number
}

const toasts = ref<Toast[]>([])
let counter = 0
function push(type: ToastType, message: string, timeout = 3000) {
  const id = ++counter
  toasts.value.push({ id, type, message, timeout })
  if (timeout > 0) setTimeout(() => remove(id), timeout)
  return id
}
function remove(id: number) {
  toasts.value = toasts.value.filter((t) => t.id !== id)
}
export function useToast() {
  return {
    toasts,
    success: (m: string, t?: number) => push('success', m, t),
    error: (m: string, t?: number) => push('error', m, t),
    info: (m: string, t?: number) => push('info', m, t),
    remove,
  }
}
