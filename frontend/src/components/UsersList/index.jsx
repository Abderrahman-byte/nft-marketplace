import React from 'react'

import UserCard from '@Components/UserCard'

import './styles.css'

const UsersList = ({ data = [] }) => {
    return (
        <div className='UsersList'>
            {data.map((user, i) => <UserCard key={i} {...user} />)}
        </div>
    )
}

export default UsersList