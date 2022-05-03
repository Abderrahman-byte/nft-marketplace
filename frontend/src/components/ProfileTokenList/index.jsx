import React, { useContext, useEffect, useState } from 'react'

import { AuthContext } from '@/context/AuthContext'
import TokenCard from '../TokenCard'
import LoadingCard from '../LoadingCard'

const TokenProfileList = ({ id, getTokensFunction }) => {
    const { openModel, closeModel } = useContext(AuthContext)
    const [tokens, setTokens] = useState([])
    const [page, setPage] = useState(1)
    const [isMore, setMore] = useState(true)
    const itemsPerCall = 3
    
    const getTokensList = async () => {
        if (page <= 0 || !isMore) return

        openModel(<LoadingCard />)

        const result = await getTokensFunction(id, itemsPerCall, (page - 1) * itemsPerCall)
        setTokens([...tokens, ...result])
    
        if (result.length < itemsPerCall) setMore(false)

        closeModel()
    }

    useEffect(() => {
        getTokensList()
    }, [page]) 

    useEffect(() => {
        setTokens([])
        setPage(0)
        setMore(true)

        setTimeout(() => setPage(1), 0)
    }, [id, getTokensFunction])

    return (
        <div className='TokenProfileList profile-content'>
            {tokens.map((token, i) => <TokenCard key={i} likable link {...token} />)}

            {isMore ? (
                <div className='more-btn-container'>
                    <button onClick={() => setPage(page + 1)} className='btn btn-blue more-btn'>More</button>
                </div>
            ) : null}
        </div>
    )
}

export default TokenProfileList