import React from 'react'
import { Link } from 'react-router-dom'

import '@Styles/AppLogo.css'

const AppLogo = ({isOpenSearch}) => {
    return (
      
        <div className={`AppLogo ${isOpenSearch? 'open':''}`}>
            <Link className='Logo-link' to='/'>
                <span className='crypter'>RNFT</span>
            </Link>
        </div>
    )
}

export default AppLogo