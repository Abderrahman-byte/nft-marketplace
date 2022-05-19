import React, { useContext } from 'react'
import { Link } from 'react-router-dom'

import AppLogo from './AppLogo'
import SearchBox from './SearchBox'
import NotificationsBar from './NotificationsBar'
import { AuthContext } from '@Context/AuthContext'

import './styles.css'
import ProfileBar from './ProfileBar'

const AppHeader = () => {
    const { authenticated } = useContext(AuthContext)

    return (
        <header className='AppHeader Nav-content'>
            <div className='Left-content'>
                <AppLogo />
                <div className='vertical-divider' />
                <Link className='nav-link' to='/discover' >Discover</Link>
                <Link className='nav-link' to='#' >How it work</Link>
            </div>
            <div className='Actions'>
                <SearchBox />
                <NotificationsBar />
                <div className='buttons'>
                    <Link className='btn btn-blue' to='/upload'>Upload</Link>
                    {authenticated ? (
                        <ProfileBar />
                    ) : (
                        <Link className='btn btn-white' to='/sign-in'>Connect Wallet</Link>
                    )}
                </div>
            </div>
        </header>
    )
}

export default AppHeader