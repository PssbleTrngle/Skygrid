import { Vector } from './types'

export function size(from: Vector, to: Vector) {
   return [to[0] - from[0], to[1] - from[1], to[2] - from[2]] as Vector
}
