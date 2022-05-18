import { getPopularSellersList } from '@Utils/api'
import React, { useEffect, useState } from 'react'
import PopularSellerCard from './PopularSellerCard'

import './styles.css'

const PopularSellersList = () => {
	const [sellers, setSellers] = useState([])

	const fetchPopularSellers = async () => {
		const sellers = await getPopularSellersList()
		setSellers(sellers)
	}

	useEffect(() => {
		fetchPopularSellers()
	}, [])

	return (
		<div className='PopularSellersList'>
			<div className='header'>
				<div className='title'>
					<h4>Popular</h4>
					<h3>Sellers</h3>
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
