import React from 'react'
import { Link } from 'react-router-dom'

import './styles.css'

const AvatarLink = ({ title, img, name, to = '#', size='small' }) => {
	return (
		<div className='AvatarLink'>
			<Link className='block' to={to}>
				<img src={img} />
			</Link>
			<div className={`AvatarLink-info AvatarLink-info-${size}`}>
				<h6>{title}</h6>
				<span className='name'>{name}</span>
			</div>
		</div>
	)
}

export default AvatarLink
