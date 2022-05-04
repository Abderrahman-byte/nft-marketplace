import React, { useContext, useEffect, useState } from 'react'
import { Link } from 'react-router-dom'

import CreateSingleItemForm from '@Components/CreateTokenForm'

import '@Styles/UploadItem.css'
import TokenCard from '@/components/TokenCard'
import { AuthContext } from '@/context/AuthContext'
import LoadingCard from '@/components/LoadingCard'
import { createToken } from '@/utils/api'

const UploadSinglePage = () => {
	const { account, openModel, closeModel } = useContext(AuthContext)

	const [tokenData, setTokenData] = useState({ price: 0.0 })

	const onUpdateCallback = (data) => {
		setTokenData({...tokenData, ...data})
	}

	const createItemCallback = async (file, metadata) => {
		openModel(<LoadingCard />)

		const response = await createToken(file, metadata)

		console.log(response)

		closeModel()
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
                    <CreateSingleItemForm onSubmitCallback={createItemCallback} onUpdateCallback={onUpdateCallback} />
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
