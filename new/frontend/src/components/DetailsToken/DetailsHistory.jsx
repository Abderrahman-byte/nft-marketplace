import React, { useContext, useEffect, useState } from 'react'

import { getHistoryTransaction } from '@Utils/api'
import HistoDiv from './Histodiv'

import './styles.css'

const DetailsHistory = ({ Id }) => {
	const [transaction, setTransaction] = useState([])
	const [page, setPage] = useState(1)
	const [isMore, setMore] = useState(true)
    const [isLoading, setLoading] = useState(false)
	const transshwonFirst = 4

	const getTransaction = async () => {
		if (page <= 0 || !isMore) return
		const result = await getHistoryTransaction(
			Id,
			transshwonFirst,
			(page - 1) * transshwonFirst
		)
		setTransaction([...transaction, ...result])
		if (result.length < transshwonFirst) setMore(false)
	}

	useEffect(() => {
		setTransaction([])
		setPage(0)
		setMore(true)

		setTimeout(() => setPage(1), 0)
	}, [Id, getHistoryTransaction])
	useEffect(() => {
		getTransaction()
	}, [page])

	const handleScroll = (e) => {
		const subElt = e.target.querySelector('.bids-scroll')
        const realHeight = Number.parseFloat(getComputedStyle(subElt).height)
        const scrollPosition = e.target.scrollTop
        const eltHeight = Number.parseFloat(getComputedStyle(e.target).height)

        if (eltHeight + scrollPosition >= realHeight - 1 && !isLoading && isMore) {
            setPage(page + 1)            
        }
	}

	return (
		<div className='bids-cont' onScroll={handleScroll}>
			<div className='bids-scroll'>
				{transaction.map((bid, i) => (
					<HistoDiv key={i} {...bid} />
				))}
			</div>
		</div>
	)
}
export default DetailsHistory
