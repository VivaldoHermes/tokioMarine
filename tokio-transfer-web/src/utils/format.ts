export function formatMoneyBRL(n: number | null | undefined): string {
  const v = typeof n === 'number' && !Number.isNaN(n) ? n : 0
  try {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(v)
  } catch {
    return String(v)
  }
}

export function parseMoneyBRL(input: string | number | null | undefined): number {
  if (typeof input === 'number') return Number.isFinite(input) ? input : 0
  if (input == null) return 0
  let s = String(input).trim()
  if (!s) return 0
  s = s.replace(/[R$\s]/gi, '')
  s = s.replace(/\./g, '').replace(/,/g, '.')
  const n = parseFloat(s)
  return Number.isFinite(n) ? n : 0
}

export function formatISODateYYYYMMDD(iso: string | null | undefined): string {
  if (!iso) return ''
  if (/^\d{4}-\d{2}-\d{2}$/.test(iso)) return iso
  const d = new Date(iso)
  if (Number.isNaN(d.getTime())) return String(iso)
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}
