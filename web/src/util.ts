export function delay<T>(data: T, error = true) {
   return () =>
      new Promise<T>((res, rej) => {
         setTimeout(() => {
            if (error && Math.random() < 0.1) rej(new Error('an error occured'))
            else res(data)
         }, 100 + 300 * Math.random())
      })
}
