import React from 'react'

import { formatMoney } from '@Utils/currency'
import { formatLapse } from '@Utils/generic'
import { Link } from 'react-router-dom'

const BidCreatedCard = ({details, id, vued, onClickCallback}) => {
    return (
        <Link onClick={() => onClickCallback(id)} to={`/details/${details?.token?.id}`} className='NotificationCard'>
			<img src={details?.from?.avatarUrl || details?.token?.previewUrl}></img>
			<div className='info'>
				<h6 className='title'>A bid has been place on {details?.token?.title}</h6>
				<p className='detail'>{details?.from?.displayName} offered {formatMoney(details?.price)} RVN for {details?.token?.title}</p>
				<p className='created_date'>{formatLapse(details?.createdDate || Date.now())}</p>
			</div>
			<div className={`is-new ${!vued ? 'active': ''}`}></div>
		</Link>
    )
}

export default BidCreatedCard