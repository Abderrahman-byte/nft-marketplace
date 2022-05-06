import React from 'react'
import { Link } from 'react-router-dom'

import '@Styles/AppLogo.css'

const AppLogo = () => {
    return (
        <div className='AppLogo'>
            <Link className='Logo-link' to='/'>
                <span className='crypter'>RNFT</span>
            </Link>
        </div>
    )
}

export default AppLogo