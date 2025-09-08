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

export function formatISODateDDMMYYYY(
  iso: string | null | undefined,
  sep: '-' | '/' = '-',
): string {
  if (!iso) return ''
  const s = String(iso)

  const m = s.match(/^(\d{4})-(\d{2})-(\d{2})/)
  if (m) return `${m[3]}${sep}${m[2]}${sep}${m[1]}`

  const d = new Date(s)
  if (Number.isNaN(d.getTime())) return s
  const dd = String(d.getDate()).padStart(2, '0')
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const yyyy = d.getFullYear()
  return `${dd}${sep}${mm}${sep}${yyyy}`
}
