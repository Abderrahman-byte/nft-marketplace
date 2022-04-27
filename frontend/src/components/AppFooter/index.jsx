import React from 'react'
import { Link } from 'react-router-dom'

import AppLogo from '@Components/AppHeader/AppLogo'
import NewsletterInput from './NewsletterInput'

import '@Styles/AppFooter.css'

const AppFooter = () => {
    return (
        <footer className='AppFooter'>
            <div className='AppFooter-container'>
                <div className='top-bar'>

                    <div className='Frame-774'>
                        <AppLogo />
                        <span className='motto'>The New Creative Economy.</span>
                    </div>

                    <div className='Frame-783'>
                        <h6>RNFT</h6>
                        <div className='links-list'>
                            <Link to='#'>Discover</Link>
                            <Link to='#'>Connect Wallet</Link>
                            <Link to='#'>Create Item</Link>
                        </div>
                    </div>

                    <div className='Frame-783'>
                        <h6>Info</h6>
                        <div className='links-list'>
                            <Link to='#'>Download</Link>
                            <Link to='#'>Demos</Link>
                            <Link to='#'>Support</Link>
                        </div>
                    </div>

                    <div className='Frame-783 newsletter'>
                        <h6>Join Newsletter</h6>
                        <span className='newsletter-msg'>Subscribe our newsletter to get more free design course and resource</span>
                        <NewsletterInput />
                    </div>

                </div>
                <div className='bottom-bar'>
                    <p>Copyright © 2021 UI8 LLC. All rights reserved</p>
                </div>
            </div>
        </footer>
    )
}

export default AppFooter