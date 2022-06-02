import { formatMoney } from '@Utils/currency'
import { formatLapse } from '@Utils/generic'
import React from 'react'
import { Link } from 'react-router-dom'

const SoldNotificationCard = ({ details , onClickCallback, id, vued }) => {
	return (
		<Link onClick={() => onClickCallback(id)} to={`/details/${details?.token?.id}`} className='NotificationCard'>
			<img src={details?.to?.avatarUrl || details?.token?.previewUrl}></img>
			<div className='info'>
				<h6 className='title'>NFT {details?.token?.title} has been sold</h6>
				<p className='detail'>Sold to {details?.to?.displayName} for {formatMoney(details?.price)} RVN</p>
				<p className='created_date'>{formatLapse(details?.createdDate || Date.now())}</p>
			</div>
			<div className={`is-new ${!vued ? 'active': ''}`}></div>
		</Link>
	)
}

export default SoldNotificationCard