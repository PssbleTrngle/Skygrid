export function exists<T>(t: T | undefined | null): t is T {
   return (t ?? null) !== null
}
