import styled from "styled-components";
import Link from "./basic/Link";
import { FC } from "react";

export interface CIProps {
  repository: string;
  sha?: string;
  version: string;
}

const SourceDisplay: FC<CIProps> = ({ repository, sha, version }) => (
  <>
    {repository}@
    {sha ? (
      <RefLink href={`https://github.com/${repository}/tree/${sha}/web`}>
        {version}
      </RefLink>
    ) : (
      <span>{version}</span>
    )}
  </>
);

const RefLink = styled(Link).attrs({ underline: "always" })`
  color: ${(p) => p.theme.accent};
`;

export default SourceDisplay;
