import Image from 'next/image'
import { useEffect, useMemo, useState, VFC } from 'react'
import { Block } from '../../@types/BlockProviders'

const FALLBACK = 'unknown_block.png'

const BlockIcon: VFC<Block & { size: number }> = ({ size, id, icon }) => {
   const defaultSrc = useMemo(() => icon ?? FALLBACK, [icon])
   const [src, setSrc] = useState<string>(defaultSrc)

   useEffect(() => setSrc(defaultSrc), [defaultSrc])

   return (
      <Image
         objectFit='contain'
         alt={id}
         src={`/${src}`}
         height={size}
         width={size}
         onError={() => setSrc(FALLBACK)}
      />
   )
}

export default BlockIcon
