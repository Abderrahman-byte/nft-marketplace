import React, { useEffect, useState } from 'react'

import { getPopularSellersList } from '@Utils/api'
import PopularSellerCard from './PopularSellerCard'
import Select from 'react-select'

import './styles.css'

const filterOptions = [
	{ value: 'DAY', label: 'Today'},
	{ value: 'WEEK', label: 'Last Week'},
	{ value: 'MONTH', label: 'Last Month'},
]

const PopularSellersList = () => {
	const [sellers, setSellers] = useState([])
	const [filterBy, setFilter] = useState({...filterOptions[0]})

	const fetchPopularSellers = async () => {
		const sellers = await getPopularSellersList(filterBy.value)
		setSellers(sellers)
	}

	useEffect(() => {
		fetchPopularSellers()
	}, [filterBy])

	return (
		<div className='PopularSellersList'>
			<div className='header'>
				<div className='title'>
					<h4>Popular</h4>
					<h3>Sellers</h3>
				</div>

				<div className='filter'>
					<label className='filter-label'>timeframe</label>
					<Select
						className='filter-select'
						options={filterOptions}
						value={filterBy}
						onChange={setFilter}
					/>
				</div>
			</div>
			<div className='sellers-container'>
				{sellers.map((user, i) => (
					<PopularSellerCard key={i} rank={i + 1} {...user} />
				))}
			</div>
		</div>
	)
}

export default PopularSellersList
