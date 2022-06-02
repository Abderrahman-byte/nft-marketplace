import React, {useState} from 'react'
import { NavLink } from 'react-router-dom'
import Select from 'react-select'

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
    const filterOptions = links.map((link, i) => ({label:<NavLink key={i} to={link.link} >{link.text}</NavLink> ,
                                                   value: link.text}))
    const [filterBy, setFilter] = useState({...filterOptions[0]})
    return (
        <nav className='ProfileNavbar'>
            {links.map((link, i) => <NavLink className={'for-res'} key={i} to={link.link} >{link.text}</NavLink>)}
             <Select 
            className='select'
						options={filterOptions}
						value={filterBy}
						onChange={setFilter}
					/>
        </nav>
       /* <nav className='ProfileNavbar'>
            <Select 
            className='select'
						options={filterOptions}
						value={filterBy}
						onChange={setFilter}
					/>
        </nav>*/
    )
}

export default ProfileNavbar