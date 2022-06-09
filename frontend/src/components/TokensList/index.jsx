import TokenCard from '@Components/TokenCard'
import React from 'react'

import './styles.css'

const TokensList = ({ data = [] }) => {
    return (
        <div className='TokensList'>
            {data.map((token, i) => <TokenCard key={i} likable link {...token} />)}
        </div>
    )
}

export default TokensList