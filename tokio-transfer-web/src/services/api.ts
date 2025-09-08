import axios, { AxiosError, AxiosInstance, AxiosResponse } from 'axios'

export interface ApiError {
  status?: number
  message: string
  raw: unknown
}

const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
  timeout: 15_000,
})

api.interceptors.response.use(
  (response: AxiosResponse): AxiosResponse => response,
  (error: unknown) => {
    if (axios.isAxiosError(error)) {
      const status = error.response?.status
      const data = error.response?.data as { message?: string; error?: string } | undefined
      const message = data?.message ?? data?.error ?? error.message ?? 'Request failed'
      const normalized: ApiError = { status, message, raw: error as AxiosError }
      return Promise.reject(normalized)
    }
    const normalized: ApiError = { status: undefined, message: 'Unknown error', raw: error }
    return Promise.reject(normalized)
  },
)

export default api
