import React from 'react'

import { formatMoney } from '@Utils/currency'
import { formatLapse } from '@Utils/generic'
import { Link } from 'react-router-dom'

const BidRejectedCard = ({details, id, vued, onClickCallback, createdDate}) => {
    return (
        <Link onClick={() => onClickCallback(id)} to={`/details/${details?.token?.id}`} className='NotificationCard'>
			<img src={details?.token?.previewUrl}></img>
			<div className='info'>
				<h6 className='title'>Your bid on {details?.token?.title} has been rejectd</h6>
				<p className='detail'>{details?.to?.displayName} has reject your {formatMoney(details?.offer)} RVN offer on {details?.token?.title}</p>
				<p className='created_date'>{formatLapse(createdDate || Date.now())}</p>
			</div>
			<div className={`is-new ${!vued ? 'active': ''}`}></div>
		</Link>
    )
}

export default BidRejectedCard