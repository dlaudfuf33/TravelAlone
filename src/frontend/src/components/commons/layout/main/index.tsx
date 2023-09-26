import React from "react";
import {
  LaptopOutlined,
  NotificationOutlined,
  UserOutlined,
} from "@ant-design/icons";
import type { MenuProps } from "antd";
import { Breadcrumb, Layout, Menu, theme } from "antd";
import LayoutBanner from "../banner/LayoutBanner.container";
import { useRouter } from "next/router";

const { Content, Sider } = Layout;

// 게시판 이름 배열
const boardNames = ["자유 게시판", "여행지 추천", "주변 놀거리"];

const App: React.FC = () => {
  const router = useRouter();

  const items2: MenuProps["items"] = [
    UserOutlined,
    LaptopOutlined,
    NotificationOutlined,
  ].map((icon, index) => {
    const key = String(index + 1);

    return {
      key: `sub${key}`,
      icon: React.createElement(icon),
      label: boardNames[index], // 게시판 이름

      children:
        index === 0 // 첫 번째 메뉴인 경우
          ? [
              {
                key: "1",
                label: "게시글 작성",
                onClick: async () => await router.push("/boards/new"),
              },
              {
                key: "2",
                label: "게시글 목록",
                onClick: async () => await router.push("/boards"),
              },
              // If the route is not a part of your Next.js app use window.location.href instead.
              // This is because Next.js's Router only handles routes within your app.
              // If it's an external link or a link to a static file you should use an <a> tag with href.
              {
                key: "3",
                label: "추천 많은 글",
                onClick: () => (window.location.href = "/recommended"),
              },
            ]
          : new Array(4).fill(null).map((_, j) => {
              const subKey = index * 4 + j + 1;
              return {
                key: subKey.toString(),
                label: `option${subKey}`,
              };
            }),
    };
  });

  const {
    token: { colorBgContainer },
  } = theme.useToken();

  return (
    <Layout>
      <Layout>
        <Sider width={200} style={{ background: colorBgContainer }}>
          <Menu
            mode="inline"
            defaultSelectedKeys={["1"]}
            defaultOpenKeys={["sub1"]}
            style={{ height: "100%", borderRight: 0 }}
            items={items2}
          />
        </Sider>
        <Layout style={{ padding: "0 24px 24px" }}>
          <Breadcrumb style={{ margin: "16px 0" }}>
            <Breadcrumb.Item>Home</Breadcrumb.Item>
            <Breadcrumb.Item>List</Breadcrumb.Item>
            <Breadcrumb.Item>App</Breadcrumb.Item>
          </Breadcrumb>
          <Content
            style={{
              padding: 24,
              margin: 0,
              minHeight: 1000,
              background: colorBgContainer,
            }}
          >
            <LayoutBanner />
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};

export default App;
