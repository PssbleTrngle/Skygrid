import { darken } from 'polished'
import { VFC } from 'react'
import { useQuery } from 'react-query'
import styled from 'styled-components'
import { delay } from '../util'
import Button from './Button'

interface Reward {
   name: string
   price: number
   description: string
}

const Rewards: VFC = () => {
   const { data } = useQuery<Reward[]>(
      'rewards',
      delay([
         {
            name: 'Buy hotdog',
            price: 500,
            description:
               'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec odio nisl, interdum eget hendrerit ac, congue ut metus. Sed et.',
         },
         {
            name: 'Go to the pool',
            price: 500,
            description:
               'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis facilisis, nibh id feugiat gravida, tellus ipsum laoreet ligula, non tincidunt enim urna nec.',
         },
      ])
   )

   return (
      <Style>
         {data?.map(({ name, description, price }) => (
            <Panel key={name}>
               <h3>{name}</h3>
               <small>{price} smackles</small>
               <Desc>{description}</Desc>
               <Button>Buy</Button>
            </Panel>
         ))}
      </Style>
   )
}

const Style = styled.section`
   display: grid;
   gap: 1rem;
`

const Desc = styled.p`
   grid-area: desc;
`

const Panel = styled.div`
   border-radius: 1rem;
   background: ${p => darken(0.05, p.theme.bg)};
   padding: 0.5rem;

   display: grid;
   align-items: center;

   grid-template:
      'name price buy'
      'desc desc desc';
`

export default Rewards
