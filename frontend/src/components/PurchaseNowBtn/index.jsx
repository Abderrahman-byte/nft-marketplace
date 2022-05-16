import React, { useCallback, useContext } from 'react'

import LoadingCard from '@Components/LoadingCard'
import MessageCard from '@Components/MessageCard'
import LoginPage from '@Pages/Login.page'
import QrCodeCard from '@Components/QrCodeCard'
import { AuthContext } from '@Context/AuthContext'
import { buildApiUrl, createTransaction } from '@Utils/api'
import { DEFAULT_ERROR } from '@Utils/generic'

const PurchaseNowBtn = ({ tokenId }) => {
	const { authenticated, account, openModel, closeModel } = useContext(AuthContext)

	const ShowError = (msg) =>
		openModel(
			<MessageCard title='Could not buy token' text={msg} closeBtnCallback={closeModel} />,
			closeModel
		)

	const PurchaseNowCallback = useCallback(async () => {
		if (!authenticated || !account) return <LoginPage />

		openModel(<LoadingCard />)

		const [ref, error] = await createTransaction({ id: tokenId })

		if (ref === null) return ShowError(error?.detail || DEFAULT_ERROR.message)

		const eventSource = new EventSource(buildApiUrl('/marketplace/buy') + `?ref=${ref}`, { withCredentials: true })

        const closeModelQr = () => {
			if (eventSource.readyState !== eventSource.CLOSED) eventSource.close()
			closeModel()
		}

		eventSource.addEventListener('qr', (event) => {
			const data = event.data

            if (data) {
                openModel(
                    <QrCodeCard
                        title='Scan to confirm'
                        text='Confirm by scanning this code'
                        value={data}
                        closeBtnCallback={closeModelQr}
                    />,
                    closeModelQr
                )
            } else {
                console.log('no Data found for the qr')
            }
		})

		eventSource.addEventListener('accepted', () => {
			openModel(
				<MessageCard
					title='Success'
					text='The transaction is done successfully'
					closeBtnCallback={closeModel}
				/>,
				closeModel
			)

			if (eventSource.readyState !== eventSource.CLOSED) eventSource.close()
		})

		eventSource.addEventListener('error', (event) => {
			const error = JSON.parse(event.data || '{}')

			if (event.readyState !== event.CLOSED) event.close()

			ShowError(error.detail)
		})
	}, [tokenId, authenticated, account])

	return (
		<button onClick={PurchaseNowCallback} className='btn btn-blue'>Purchase now</button>
	)
}

export default PurchaseNowBtn