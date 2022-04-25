import React from 'react'
import { Link } from 'react-router-dom'

import AppLogo from './AppLogo'
import SearchBox from './SearchBox'

import '../styles/AppHeader.css'
import NotificationsBar from './NotificationsBar'

const AppHeader = () => {
    return (
        <header className='AppHeader Nav-content'>
            <div className='Left-content'>
                <AppLogo />
                <div className='vertical-divider' />
                <Link className='nav-link' to='#' >Discover</Link>
                <Link className='nav-link' to='#' >How it work</Link>
            </div>
            <div className='Actions'>
                <SearchBox />
                <NotificationsBar />
                <div className='buttons'>
                    <Link className='btn btn-blue' to='#'>Upload</Link>
                    <Link className='btn btn-white' to='#'>Connect Wallet</Link>
                </div>
            </div>
        </header>
    )
}

export default AppHeader