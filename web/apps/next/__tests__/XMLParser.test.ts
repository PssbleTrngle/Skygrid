import { ResourceType, TagDefinition } from "parser/XMLParser";
import TestingXMLParser from "parser/TestingXMLParser";
import unwrapBlocks from "ui/util/unwrapBlocks";

const parser = new TestingXMLParser();

const testTagDefinition: TagDefinition = {
  values: [
    "testing:duplicate",
    "testing:tagged_block",
    "testing:another_tagged_block",
  ],
};
parser.files.put(
  ["data", "testing", "tags", "blocks", "test_tag.json"],
  JSON.stringify(testTagDefinition)
);

parser.files.put(
  ["data", "testing", "skygrid", ResourceType.CONFIG, "test_config.xml"],
  `
      <dimension>
         <blocks>
            <tag id="test_tag" mod="testing" />
            <list weight="0.01">
               <block mod="testing" id="something"/>
               <block mod="testing" id="duplicate"/>
               <block mod="testing" id="something_else"/>
            </list>
         </blocks>
      </dimension>
   `
);

describe("XMLParser", () => {
  it("finds skygrid config files", async () => {
    const configs = await parser.getResources(ResourceType.CONFIG);
    expect(configs.length).not.toBe(0);
  });

  it("can parse the config file", async () => {
    const parsed = await parser.getConfig({
      mod: "testing",
      id: "test_config",
    });
    expect(parsed).not.toBeNull();
  });

  it("unwrappes the correct amount of blocks", async () => {
    const parsed = await parser.getConfig({
      mod: "testing",
      id: "test_config",
    });
    const unwrapped = unwrapBlocks(parsed?.blocks ?? []);
    const block = (id: string) => unwrapped.find((it) => it.id === id);

    expect(parsed).not.toBeNull();
    expect(unwrapped.length).toBe(5);
    expect(block("something")).not.toBeNull();
    expect(block("something")?.occurrences).toBe(1);
    expect(block("duplicate")?.occurrences).toBe(2);
  });

  it("unwrappes with the correct weights", async () => {
    const parsed = await parser.getConfig({
      mod: "testing",
      id: "test_config",
    });
    const unwrapped = unwrapBlocks(parsed?.blocks ?? []);
    const weightOf = (id: string) =>
      unwrapped.find((it) => it.id === id)?.weight;

    expect(parsed).not.toBeNull();
    expect(weightOf("something")).toBeCloseTo(0.01 / 3);
    expect(weightOf("duplicate")).toBeCloseTo(0.01 / 3 + 1 / 3);
  });
});
