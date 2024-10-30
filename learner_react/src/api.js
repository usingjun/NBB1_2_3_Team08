import axios from "axios";

const API_URL = "http://localhost:8080/api/v1";

export const fetchOrderDetails = async (orderId) => {
    try {
        const response = await axios.get(`${API_URL}/orders/${orderId}`);
        return response.data;
    } catch (error) {
        console.error("Error fetching order details:", error);
        throw error;
    }
};

// 추가적으로 필요한 API 함수들을 여기에 추가할 수 있습니다.
