import React from 'react'
import { NavLink } from 'react-router-dom'

import './styles.css'

const ProfileNavbar = ({ prefix = '.'}) => {
    const links = [
        {
            text: 'On sale',
            link: `${prefix}/sale`
        }, {
            text: 'Collectibles',
            link: `${prefix}/collectibles`
        }, {
            text: 'Created',
            link: `${prefix}/created`
        }, {
            text: 'Likes',
            link: `${prefix}/likes`
        }, {
            text: 'Following',
            link: `null`
        }, {
            text: 'Followers',
            link: `null_`
        }
    ]

    return (
        <nav className='ProfileNavbar'>
            {links.map((link, i) => <NavLink key={i} to={link.link} >{link.text}</NavLink>)}
        </nav>
    )
}

export default ProfileNavbar