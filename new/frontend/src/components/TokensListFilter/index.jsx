import React, { useEffect, useState } from 'react'
import Select from 'react-select'

import Slider from '@Components/Slider'

import './styles.css'

const sortOptions = [
	{ value: 'LIKES', label: 'Most Liked' },
	{ value: 'HIGH_PRICE', label: 'Highest Price' },
	{ value: 'LOW_PRICE', label: 'Lower Price' },
]

const TokensListFilter = ({onFilterUpdate}) => {
	const [sortValue, setSortBy] = useState({ ...sortOptions[0] })
	const [maxPrice, setMaxPrice] = useState(100000)

	useEffect(() => {
        if (typeof onFilterUpdate !== 'function') return

		onFilterUpdate({ sort: sortValue.value, maxPrice })
	}, [sortValue, maxPrice])

	return (
		<div className='TokensListFilter'>
			<div className='form filter-controls'>
				<div className='form-div'>
					<label className='form-label'>SORT BY</label>
					<Select
						isSearchable={false}
						onChange={setSortBy}
						value={sortValue}
						options={sortOptions}
					/>
				</div>

				<div className='form-div'>
					<label className='form-label'>MAX PRICE</label>
                    <Slider min={1} max={100000} value={maxPrice} onChange={setMaxPrice} />
				</div>
			</div>
		</div>
	)
}

export default TokensListFilter
