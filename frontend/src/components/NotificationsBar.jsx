import React from 'react'

import '../styles/NotificationsBar.css'

const NotificationsBar = () => {
    return (
        <div className='NotificationsBar'>
            <button className='NotificationsBar-btn'>
                <i className='notification-icon active'></i>
            </button>
        </div>
    )
}

export default NotificationsBar