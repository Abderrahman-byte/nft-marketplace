import React, { useState } from 'react'

import './styles.css'

const CreateBidForm = ({ onSubmitCallback }) => {
	const [price, setPrice] = useState(0)

	const onSubmit = (e) => {
		e.preventDefault()

        if (price <= 0 || typeof onSubmitCallback  !== 'function') return

        onSubmitCallback(price)
	}

	return (
		<form onSubmit={onSubmit} className='CreateBidForm form form-card card'>
			<div className='form-div'>
				<h2>Place a bid</h2>
				<p>You are about to place a bid for this token.</p>
			</div>

			<div className='form-div'>
				<label className='form-label'>Your bid</label>
				<input
					value={price}
					onChange={(e) => setPrice(e.target.value)}
					type='number'
					className='form-input'
					name='price'
					placeholder='your bid'
					min={0}
					step={0.00001}
				/>
			</div>

			<button className='btn btn-white block'>Place bid</button>
		</form>
	)
}

export default CreateBidForm
