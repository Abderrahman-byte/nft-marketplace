import React from 'react'

import AppLogo from './AppLogo'
import { Link } from 'react-router-dom'

import '../styles/AppHeader.css'

const AppHeader = () => {
    return (
        <header className='AppHeader Nav-content'>
            <div className='Left-content'>
                <AppLogo />
                <div className='horizontal-divider' />
                <Link className='nav-link' to='#' >Discover</Link>
                <Link className='nav-link' to='#' >How it work</Link>
            </div>
            <div className='Actions'></div>
        </header>
    )
}

export default AppHeader