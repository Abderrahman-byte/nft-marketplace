import React, { useContext, useState } from 'react'
import { Link } from 'react-router-dom'

import LoadingCard from '@Components/LoadingCard'
import { AuthContext } from '@Context/AuthContext'
import { formatMoney } from '@Utils/currency'
import { sendLogout } from '@Utils/api'

const ProfileBar = () => {
	const { account, setAuth, openModel, closeModel } = useContext(AuthContext)
    const [isOpen, setOpen] = useState(false)

    const logout = async () => {
        setOpen(false)

        openModel(<LoadingCard />)
        await sendLogout()
        
        closeModel()
        setAuth(false)
    }

    if (!account) return <></>

	return (
		<div className='ProfileBar'>
			<button onClick={() => setOpen(!isOpen)} className='ProfileBar-btn'>
				<img className='avatar' src={account?.avatarUrl} />
				<span className='balance'>
					{formatMoney(account?.balance || 700698)}{' '}
					<span className='currency'>RVN</span>
				</span>
			</button>

            {isOpen ? (
                <>
                    <div onClick={() => setOpen(false)} className='click-detector'></div>
                    <div className='ProfileBar-popup'>
                        <h6 className='name'>{account?.displayName}</h6>
                        <div className='balance-card'>
                            <div className='balance'>
                                <div className='vector'></div>
                                <div className='detail'>
                                    <h6>Balance</h6>
                                    <span className='balance-value'>{formatMoney(4700.698)} ETH</span>
                                </div>
                            </div>
                            <Link className='btn btn-white block' to=''>Manage fun on Stibits</Link>
                        </div>

                        <nav className='account-navbar'>
                            <Link onClick={() => setOpen(false)} to='/profile' className='block profile-link'>
                                <i className='icon-user'></i><span>My profile</span>
                            </Link>
                            <Link onClick={() => setOpen(false)} to='/profile/collectibles' className='block profile-link'>
                                <i className='icon-image'></i><span>My items</span>
                            </Link>
                            <Link to='/discover' className='block profile-link res'> <span>Discover </span></Link>
                            <Link  to='#' className='block profile-link res'> <span> How it work </span></Link>
                        
                            <button onClick={logout} className='block profile-link'>
                                <i className='icon-logout'></i><span>Disconnect</span>
                            </button>
                        </nav>
                    </div>
                </>
            ) : null}
		</div>
	)
}

export default ProfileBar
