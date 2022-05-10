import React, { useEffect, useRef } from 'react'

const QrCodeCard = ({ title, text}) => {
    const qrCodeRef = useRef()

    useEffect(() => {
        if (!qrCodeRef.current) return

        console.log(QRCode)

        // const qr = new QRCode(qrCodeRef.current)
        // qr.makeCode('Abderrahmane')

    }, [qrCodeRef])

    return (
        <div className='card'>
            <h2>{title}</h2>
            <p>Scan code with stibits wallet</p>
            <small>{text}</small>

            <div className='qrcode-container'></div>
        </div>
    )
}

export default QrCodeCard