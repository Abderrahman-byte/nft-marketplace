import React from 'react'
import { Link } from 'react-router-dom'

import './styles.css'

const UserCard = ({ id, displayName, avatarUrl, username }) => {
    
    return (
        <Link to={`/user/${id}/sale`} className='UserCard'>
            <img className='avatar' src={avatarUrl} />
            <span className='name'>{displayName || username}</span>
        </Link>
    )
}

export default UserCard