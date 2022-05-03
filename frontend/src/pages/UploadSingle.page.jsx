import React, { useContext, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

import CreateSingleItemForm from '@Components/CreateTokenForm'

import '@Styles/UploadItem.css'
import TokenCard from '@/components/TokenCard'
import { AuthContext } from '@/context/AuthContext'

const UploadSinglePage = () => {
	const { account } = useContext(AuthContext)

	const [tokenData, setTokenData] = useState({ price: 0.0 })

	const onUpdateCallback = (data) => {
		setTokenData({...tokenData, ...data})
	}

	useEffect(() => {
		setTokenData({ ...tokenData, owner: account, creator: account })
	}, [account])

	return (
		<div className='UploadSinglePage UploadItem'>
			<div className='container'>
				<div className='left-side'>
                    <div className='form-header'>
                        <h2>Create Single collectible</h2>
                        <Link className='btn btn-white' to='#'>Switch to multiple</Link>
                    </div>
                    <CreateSingleItemForm onUpdateCallback={onUpdateCallback} />
				</div>
				<div className='preview'>
					<h2>Preview</h2>
					<TokenCard {...tokenData} />
					<button className='btn clean-all-btn'>
						<i className='timer-icon'></i>
						<span>Clean all</span>
					</button>
				</div>
			</div>
		</div>
	)
}

export default UploadSinglePage
