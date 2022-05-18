import React, { useCallback, useContext, useState } from 'react'
import { Link } from 'react-router-dom'

import { NotificationsContext } from '@Context/NotifiactionsContext'
import SoldNotificationCard from '@Components/NotificationCards/SoldNotificationCard'
import BidCreatedCard from '@Components/NotificationCards/BidCreatedCard'
import BidAcceptedCard from '@Components/NotificationCards/BidAcceptedCard'
import BidRejectedCard from '@Components/NotificationCards/BidRejectedCard'

const getNotificationBars = (notifications, rest) => {
	return notifications.map((data, i) => {
		const props = {...data, ...rest}

		if (data.event === 'SOLD') {
			return (
				<SoldNotificationCard key={i} {...props} />
			)
		} 

		if (data.event === 'BID_CREATED') {
			return (
				<BidCreatedCard key={i} {...props} />
			)
		}

		if (data.event === 'BID_ACCEPTED') {
			return (
				<BidAcceptedCard key={i} {...props} />
			)
		}

		if (data.event === 'BID_REJECT') {
			return (
				<BidRejectedCard key={i} {...props} />
			)
		}

		return (<span key={i}></span>)
	})
}

const NotificationsBar = () => {
	const { notifications, sendVuedEvent, setVued } = useContext(NotificationsContext)
	const [isOpen, setOpen] = useState(false)

	const hasNotifications = useCallback(() => {
		if (!notifications || notifications.length <= 0) return false

		return notifications.some((notification) => !notification.vued)
	}, [notifications])

	const openNotifications = useCallback(() => {
		if (!isOpen && notifications.length > 0) sendVuedEvent(notifications[0]?.createdDate || Date.now())

		setOpen(!isOpen)
	}, [isOpen, setOpen, notifications])

	return (
		<div className='NotificationsBar'>
			<button onClick={openNotifications} className='NotificationsBar-btn' >
				<i className={`notification-icon ${hasNotifications() ? 'active' : null}`}></i>
			</button>

			{isOpen ? (
				<>
					<div onClick={() => setOpen(false)} className='click-detector'></div>
					<div className='NotificationsBar-popup'>
						<div className='popup-header'>
							<h5>Notification</h5>
							<Link className='btn btn-blue block' to='#'>
								See all
							</Link>
						</div>
						{getNotificationBars(notifications, {
							onClickCallback: (id) => {
								setVued(id)
								setOpen(false)
							},
						})}
					</div>
				</>
			) : null}
		</div>
	)
}

export default NotificationsBar
