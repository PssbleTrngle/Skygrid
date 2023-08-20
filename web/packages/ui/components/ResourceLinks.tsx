import { groupBy } from "lodash-es";
import { FC, useMemo } from "react";
import styled from "styled-components";
import { Named } from "schema/generated/types";
import { ButtonStyle } from "./basic/Button";
import Link from "./basic/Link";

const ResourceLinks: FC<{ keys: Named[] }> = ({ keys }) => {
  const grouped = useMemo(
    () => Object.entries(groupBy(keys, (it) => it.mod)),
    [keys]
  );

  return (
    <Style>
      {grouped.map(([mod, keys]) => (
        <Group key={mod}>
          <h3>
            <i>{mod}</i>
          </h3>
          <ul>
            {keys.map(({ id }) => (
              <Link underline="none" key={id} href={`./${mod}/${id}`}>
                <ResourceLink>{id}</ResourceLink>
              </Link>
            ))}
          </ul>
        </Group>
      ))}
    </Style>
  );
};

const Group = styled.div`
  padding: 2em;

  h3 {
    margin-bottom: 1.5em;
  }

  ul {
    margin-left: 2em;
    display: grid;
    gap: 1em;

    li {
      position: relative;

      &::before,
      &::after {
        content: "";
        position: absolute;
      }

      &::before {
        left: -0.75em;
        top: calc(-50% - 0.6em);
        height: calc(100% + 0.6em);
        border-left: 2px solid ${(p) => p.theme.text};
      }

      &::after {
        width: 0.5em;
        left: -0.75em;
        top: 25%;
        height: 25%;
        border: 2px solid transparent;
        border-bottom-color: ${(p) => p.theme.text};
      }
    }

    & > :first-child {
      li::before {
        top: -0.5em;
        height: calc(100% - 0.6em);
      }
    }
  }
`;

const ResourceLink = styled.li`
  ${ButtonStyle};
  font-size: 2em;
  padding: 0.5em 1em;
`;

const Style = styled.div``;

export default ResourceLinks;
