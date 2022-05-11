import React, { useEffect, useState } from 'react'

import Carossel from '@/components/Carossel'
import TokenCard from '@/components/TokenCard'
import { getCollectionsList, getTokens } from '@/utils/api'

import '@Styles/MainPage.css'
import CollectionCard from '@/components/CollectionCard'
import MostPopularToken from '@/components/MostPopularToken'

const MainPage = () => {
    const [popularTokens, setPopularTokens] = useState([])
    const [collections, setCollections] = useState([])
    const [trendToken, setTrendToken] = useState(null)
    const [childrenWidth, setChildrenWidth] = useState(100)
    const [childrenGap, setChildrenGap] = useState(0)

    const fetchData = async () => {
        const tokens = await getTokens('POPULARE', 100000, 100)
        const forSaleTokens = tokens.filter(token => token.isForSale && !token.instantSale)

        setPopularTokens(tokens)
        setTrendToken(forSaleTokens.length > 0 ? forSaleTokens[0] : null)

        const collectionsList = await getCollectionsList(3, true)
        setCollections(collectionsList)
    }

    useEffect(() => {
        fetchData()
    }, [])

    return (
        <div className='page MainPage'>
            {trendToken ? (
                <div>
                    <MostPopularToken {...trendToken} />
                </div>
            ): null}

            <Carossel title='Popular' childrenWidthPercentage={22.85} gapPersentage={2.84} setGap={setChildrenGap} setChildrenWidth={setChildrenWidth}>
                {popularTokens.map((token, i) => <TokenCard style={{ minWidth: childrenWidth + 'px', marginRight: childrenGap + 'px'}} key={i} likable link {...token} />)}
            </Carossel>

            <div className='section'>
                <h3>Collections</h3>
                <div className='collections-container'>
                    {collections.map((collection, i) => <CollectionCard key={i} link {...collection} />)}
                </div>
            </div>
        </div>
    )
}

export default MainPage