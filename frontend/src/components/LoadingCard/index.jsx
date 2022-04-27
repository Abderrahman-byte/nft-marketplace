import React from 'react'

import '@Styles/LoadingCard.css'

const LoadingCard = () => {
	return (
		<div className='LoadingCard card'>
			<div className='lds-spinner'>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
			</div>
		</div>
	)
}

export default LoadingCard
