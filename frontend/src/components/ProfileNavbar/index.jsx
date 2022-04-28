import React from 'react'
import { NavLink } from 'react-router-dom'

import './styles.css'

const ProfileNavbar = () => {
    const links = [
        {
            text: 'On sale',
            link: '.'
        }, {
            text: 'Collectibles',
            link: './collectibles'
        }, {
            text: 'Created',
            link: './created'
        }, {
            text: 'Likes',
            link: './likes'
        }, {
            text: 'Following',
            link: 's'
        }, {
            text: 'Followers',
            link: 's'
        }
    ]

    return (
        <nav className='ProfileNavbar'>
            {links.map((link, i) => <NavLink key={i} to={link.link} >{link.text}</NavLink>)}
        </nav>
    )
}

export default ProfileNavbar