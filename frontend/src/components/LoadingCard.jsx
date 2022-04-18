import React from 'react'

import '../styles/LoadingCard.css'

const LoadingCard = () => {
	return (
		<div className='LoadingCard card'>
			<div class='lds-spinner'>
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
