import TokenCard from '@/components/TokenCard'
import { getTokens } from '@/utils/api'
import React, { useEffect, useState } from 'react'

import TokensListFilter from '@/components/TokensListFilter'

import '@Styles/DiscoverPage.css'

const DiscoverPage = () => {
    const [tokensList, setTokensList] = useState([])
    const [page, setPage] = useState(1)
    const [sortBy, setSortBy] = useState('LIKES')
    const [maxPrice, setMaxPrice] = useState(1000000)
    const [lastMaxPriceSet, setLastMaxPrice] = useState(Date.now())
    const [isLoading, setLoading] = useState(false)
    const itemsPerPage = 12

    const fetchTokensList = async (page = 1, reset = false) => {
        setLoading(true)
        const tokens = await getTokens(sortBy, maxPrice, itemsPerPage, (page - 1) * itemsPerPage)

        if (reset) setTokensList([...tokens])
        else setTokensList([...tokensList, ...tokens])
        setLoading(false)
    }

    const onFilterUpdate = ({ sort, maxPrice: max }) => {
        if (sort !== sortBy) setSortBy(sort)
        
        if (maxPrice !== max && Date.now() - lastMaxPriceSet <= 1000) setMaxPrice(max)
        
        if (maxPrice !== max) setLastMaxPrice(Date.now())
    }

    useEffect(() => {
        fetchTokensList(page, page === 1)
    }, [page])

    useEffect(() => {
        if (page === 1) fetchTokensList(1, true)
        else setPage(1)
    }, [sortBy, maxPrice])

    return (
        <div className='DiscoverPage page'>
            <h2 className='page-title'>Discover</h2>

            <TokensListFilter onFilterUpdate={onFilterUpdate} />

            <div className='tokens-container'>
                {tokensList.map((token,i) => <TokenCard key={i} likable link {...token} />)}
            </div>

            <div className='more-btn-container'>
                {isLoading ? (
                    <button disabled className='btn btn-white btn-loading'>Load More <i className='loading-icon'></i></button>
                ): (
                    <button onClick={(e) => setPage(page + 1)} className='btn btn-blue'>Load More</button>
                )}
            </div>
        </div>
    )
}

export default DiscoverPage