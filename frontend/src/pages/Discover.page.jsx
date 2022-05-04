import TokenCard from '@/components/TokenCard'
import { getTokens } from '@/utils/api'
import React, { useEffect, useState } from 'react'

import '@Styles/DiscoverPage.css'

const DiscoverPage = () => {
    const [tokensList, setTokensList] = useState([])
    const [page, setPage] = useState(1)
    const [isLoading, setLoading] = useState(false)
    const itemsPerPage = 12

    const fetchTokensList = async () => {
        setLoading(true)
        const tokens = await getTokens('LIKES', 1000000, itemsPerPage, (page - 1) * itemsPerPage)
        setTokensList([...tokensList, ...tokens])
        setLoading(false)
    }

    useEffect(() => {
        fetchTokensList()
    }, [page])

    return (
        <div className='DiscoverPage page'>
            <h2 className='page-title'>Discover</h2>

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