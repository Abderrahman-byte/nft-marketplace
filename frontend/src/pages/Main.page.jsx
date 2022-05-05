import Carossel from '@/components/Carossel'
import TokenCard from '@/components/TokenCard'
import { getTokens } from '@/utils/api'
import React, { useEffect, useState } from 'react'

import '@Styles/MainPage.css'

const MainPage = () => {
    const [popularTokens, setPopularTokens] = useState([])
    const [childrenWidth, setChildrenWidth] = useState(100)
    const [childrenGap, setChildrenGap] = useState(0)

    const fetchPopularTokens = async () => {
        const tokens = await getTokens('LIKES', 100000, 100)
        setPopularTokens(tokens)
    }

    useEffect(() => {
        fetchPopularTokens()
    }, [])

    return (
        <div className='page MainPage'>
            <Carossel title='Popular' childrenWidthPercentage={22.85} gapPersentage={2.84} setGap={setChildrenGap} setChildrenWidth={setChildrenWidth}>
                {popularTokens.map((token, i) => <TokenCard style={{ minWidth: childrenWidth + 'px', marginRight: childrenGap + 'px'}} key={i} likable link {...token} />)}
            </Carossel>
        </div>
    )
}

export default MainPage