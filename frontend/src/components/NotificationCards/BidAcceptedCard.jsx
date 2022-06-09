import React from 'react'

import { formatMoney } from '@Utils/currency'
import { formatLapse } from '@Utils/generic'
import { Link } from 'react-router-dom'

const BidAcceptedCard = ({details, id, vued, onClickCallback, createdDate}) => {
    return (
        <Link onClick={() => onClickCallback(id)} to={`/details/${details?.token?.id}`} className='NotificationCard'>
			<img src={details?.token?.previewUrl}></img>
			<div className='info'>
				<h6 className='title'>Your bid on {details?.token?.title} has been accepted</h6>
				<p className='detail'>Congratulations, you just owned {details?.token?.title} NFT for {formatMoney(details?.offer)} RVN</p>
				<p className='created_date'>{formatLapse(createdDate || Date.now())}</p>
			</div>
			<div className={`is-new ${!vued ? 'active': ''}`}></div>
		</Link>
    )
}

export default BidAcceptedCard