import { QRCodeSVG } from 'qrcode.react'
import React from 'react'

import './styles.css'

const QrCodeCard = ({ title, text, value, closeBtnCallback }) => {
	return (
		<div className='QrCodeCard card'>
			<div className='card-header'>
				<h2>{title}</h2>
				<button onClick={closeBtnCallback} className='close-btn'>
					<i className='close-icon ' />
				</button>
			</div>

			<div className='notice'>
				<div className='red-dot'></div>
				<div className='notice-text'>
					<p>Scan code with stibits wallet</p>
					<small>{text}</small>
				</div>
			</div>

			<div className='qrcode-container'>
				<QRCodeSVG value={value} />
			</div>
		</div>
	)
}

export default QrCodeCard
