import React, { cloneElement, useEffect, useState } from 'react'

import { getBidsToken } from '@Utils/api'
import BidsDiv from './Bidsdiv'

import './styles.css'

const DetailsBid = ({ Id, owner, onAcceptedCallback}) => {
	const [bids, setBids] = useState([])
	const [page, setPage] = useState(1)
	const [isMore, setMore] = useState(true)
    const [isLoading, setLoading] = useState(false)
	const Bidscalled = 4

	const getBidsForToken = async () => {
		if (page <= 0 || !isMore || isLoading) return

        setLoading(true)

		const result = await getBidsToken(Id, Bidscalled,(page - 1) * Bidscalled)
		setBids([...bids, ...result])
		
        if (result.length < Bidscalled) setMore(false)
        
        setLoading(false)
	}

	useEffect(() => {
		getBidsForToken()
	}, [page])

	useEffect(() => {
		setBids([])
		setPage(0)
		setMore(true)
		setTimeout(() => setPage(1), 0)
	}, [Id])

	const handleScroll = (e) => {
        const subElt = e.target.querySelector('.bids-scroll')
        const realHeight = Number.parseFloat(getComputedStyle(subElt).height)
        const scrollPosition = e.target.scrollTop
        const eltHeight = Number.parseFloat(getComputedStyle(e.target).height)

        if (eltHeight + scrollPosition >= realHeight - 1 && !isLoading && isMore) {
            setPage(page + 1)            
        }
	}
    /*const removeNode = (idx) => document.getElementById(`id-${idx}`).remove();*/
	return (
		<div className='bids-cont' onScroll={handleScroll}>
			<div className='bids-scroll'>
				{bids.map((bid, i) => (
					
					<BidsDiv onAcceptedCallback={onAcceptedCallback} key={i} {...bid} owner={owner}  />
				))}
			</div>
		</div>
	)
}
export default DetailsBid
